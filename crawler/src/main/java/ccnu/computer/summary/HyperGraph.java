package ccnu.computer.summary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import Jama.Matrix;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

public class HyperGraph {
	private List<String> hyperedge_list;// 分词前的句子
	private List<String> tokenizerHyperedge_list;// 分词后的句子
	private Set<String> vertex_set;
	private List<Map.Entry<String, Double>> sortedEdgesList = null;// 按PageRankScore排序后的边
	private List<Map.Entry<String, Double>> sortedVertexsList = null;// 按PageRankScore排序后的点
	List<Integer> reSortList = new ArrayList<Integer>();// 用sinkpoint方法重排序，元素为得分由高到低的句子下标

	private Matrix B;// 边权重矩阵n*1
	private Matrix Dv;// Dv为对角矩阵，元素值为节点的度（degree）
	private Matrix De;// De为对角矩阵，元素值为超边的度（degree）
	private Matrix We;// We为对角矩阵，元素值为元素值为超边的权重
	private Matrix H;// 节点与边的关系矩阵
	public Matrix Pe;// 句子转移矩阵
	public Matrix Pv;// 词转移矩阵
	public Matrix V;// RandomWalk算法给句子排序最终稳定得分
	public Matrix U;// RandomWalk算法给词排序最终稳定得分
	private Matrix E1;// 单位矩阵，m * 1
	private Matrix E2;// 单位矩阵，n * 1
	private double a = 0.85; // damping factor

	/**
	 * 构造函数，初始化超图
	 */
	public HyperGraph(String text) {
		hyperedge_list = new ArrayList<String>();
		tokenizerHyperedge_list = new ArrayList<String>();
		vertex_set = new TreeSet<String>();

		initHyperedge(text);
		calculate_H();
		calculate_De();
	}

	/**
	 * 初始化关系矩阵H
	 */
	public void calculate_H() {
		List<String> list = new ArrayList<String>(vertex_set);// 把点集合转为list，方便后面用indexOf方法找元素下标
		List<Double> list2 = new ArrayList<Double>(); // 用来存矩阵H所有的列向量值，方便后面构造H矩阵
		for (String s : tokenizerHyperedge_list) {
			double a[] = new double[list.size()];
			String ss[] = s.split(" ");
			for (String sss : ss) {
				if (list.contains(sss)) {
					a[list.indexOf(sss)] = 1;
				}
			}
		    for (int i = 0; i < list.size(); i++) {
				list2.add(a[i]);
			}
		}
		double array_H[] = new double[vertex_set.size()
				* tokenizerHyperedge_list.size()];// 矩阵H的列向量数组长度为矩阵H的行列乘积
		for (int i = 0; i < array_H.length; i++) {
			array_H[i] = list2.get(i);
		}
		H = new Matrix(array_H, vertex_set.size());
		E1 = new Matrix(H.getRowDimension(), 1, 1.0 / H.getRowDimension());
		E2 = new Matrix(H.getColumnDimension(), 1, 1.0 / H.getColumnDimension());
		U = new Matrix(H.getRowDimension(), 1, 1.0 / H.getRowDimension());
		V = new Matrix(H.getColumnDimension(), 1, 1.0 / H.getColumnDimension());
		Dv = new Matrix(H.getRowDimension(), H.getRowDimension());// m*m矩阵
		We = new Matrix(H.getColumnDimension(), H.getColumnDimension());// n*n矩阵
		
		
	}

	/**
	 * De为对角矩阵，元素值为超边的度（degree）
	 */
	public void calculate_De() {
		De = new Matrix(H.getColumnDimension(), H.getColumnDimension());// n*n矩阵
		double array_H[][] = H.getArrayCopy();
		double degree;
		for (int n = 0; n < H.getColumnDimension(); n++) {
			degree = 0;
			for (int m = 0; m < H.getRowDimension(); m++) {
				degree += array_H[m][n];
			}
			De.set(n, n, degree);
		}

		if (De.rank() < De.getRowDimension()) { // De的秩小于行（或列），说明De中对角线上有0元素，则把度设为一个较大值（1000），方便求矩阵的逆
			for (int i = 0; i < De.getColumnDimension(); i++) {
				if (De.get(i, i) == 0) {
					De.set(i, i, 1000);
				}
			}
		}
		
	}

	/**
	 * Pe为边ei到ej的转移矩阵
	 */
	public void calculate_Pe() {
		if (Dv.rank() < Dv.getRowDimension()) { 
			for (int i = 0; i < Dv.getColumnDimension(); i++) {
				if (Dv.get(i, i) == 0) {
					Dv.set(i, i, 1000);
				}
			}
		}
		Pe = De.inverse().times(H.transpose()).times(Dv.inverse()).times(H).times(We);// P=De' * HT * Dv' * H * We
		NormalizationPe();
	}

	/**
	 * Pv为vi到vj的转移矩阵
	 */
	public void calculate_Pv() {
		if (Dv.rank() < Dv.getRowDimension()) { 
			for (int i = 0; i < Dv.getColumnDimension(); i++) {
				if (Dv.get(i, i) == 0) {
					Dv.set(i, i, 1000);
				}
			}
		}
		Pv = Dv.inverse().times(H).times(We).times(De.inverse()).times(H.transpose());// P=Dv' * H * We * De' * HT
		NormalizationPv();
	}

	/**
	 * Pe归一化
	 */
	public void NormalizationPe() {
		for (int i = 0; i < Pe.getRowDimension(); i++) {
			Pe.set(i, i, 0);
		}
		double total;
		for (int m = 0; m < Pe.getRowDimension(); m++) {
			total = 0;
			for (int n = 0; n < Pe.getColumnDimension(); n++) {
				total += Pe.get(m, n);
			}
			for (int n = 0; n < Pe.getColumnDimension(); n++) {
				if (total != 0) {
					Pe.set(m, n, Pe.get(m, n) / total);
				}
			}
		}
	}

	/**
	 * Pv归一化
	 */
	public void NormalizationPv() {
		for (int i = 0; i < Pv.getRowDimension(); i++) {
			Pv.set(i, i, 0);
		}
		double total;
		for (int m = 0; m < Pv.getRowDimension(); m++) {
			total = 0;
			for (int n = 0; n < Pv.getColumnDimension(); n++) {
				total += Pv.get(m, n);
			}
			for (int n = 0; n < Pv.getColumnDimension(); n++) {
				if (total != 0) {
					Pv.set(m, n, Pv.get(m, n) / total);
				}
			}
		}
	}

	/**
	 * 初始化超图的边和点
	 */
	public void initHyperedge(String text) {
		text = text.replace("　", "");
		List<Term> list;
		StringBuffer buff = new StringBuffer();
		String sentence[] = text.split("[\n。？！]");
		for (int i = 0; i < sentence.length; i++) {
			if (sentence[i].trim().equals("") || sentence[i].length() < 9)
				continue;
			hyperedge_list.add(sentence[i].trim());
//			System.out.println(sentence[i]);
			list = StandardTokenizer.segment(sentence[i]);
			buff.setLength(0);
			for (Term t : list) {
				if (CoreStopWordDictionary.contains(t.toString()) || t.toString().trim().equals("")) {
					continue;
				}
				vertex_set.add(t.toString());
				buff.append(t.toString() + " ");
			}
			tokenizerHyperedge_list.add(buff.toString().trim());
		}
	}

	public void testIterative() {
		boolean flag1 = false;
		boolean flag2 = false;
		Matrix M1 = U.copy();
		Matrix M2 = V.copy();
		Matrix M3, M4, M5, M6;
		int n = 0;                                   //n统计迭代次数
		while (!flag1 || !flag2) {
			iterativeReinforce();
			M3 = U.copy();
			M4 = V.copy();
			M5 = M1.minus(M3);
			M6 = M2.minus(M4);
			M1 = M3.copy();
			M2 = M4.copy();
			for (int i = 0; i < M5.getRowDimension(); i++) {
				if (Math.abs(M5.get(i, 0)) > 0.0001) {
					flag1 = false;
					break;
				}
				flag1 = true;
			}

			for (int i = 0; i < M6.getRowDimension(); i++) {
				if (Math.abs(M6.get(i, 0)) > 0.0001) {
					flag2 = false;
					break;
				}
				flag2 = true;
			}
			n++;
		}
	}

	public void iterativeReinforce() {
		Matrix M = De.inverse();
		B = M.times(M).times(H.transpose().times(U).plus(E2));
		Matrix M2 = H.times(B);
		for (int i = 0; i < M2.getRowDimension(); i++) {
			Dv.set(i, i, M2.get(i, 0));
		}
		for (int i = 0; i < B.getRowDimension(); i++) {
			We.set(i, i, B.get(i, 0));
		}
		calculate_Pe();
		V = Pe.transpose().times(V).times(a).plus(E2.times(1 - a));

		B = V;
		Matrix M1 = H.times(B);
		for (int i = 0; i < M1.getRowDimension(); i++) {
			Dv.set(i, i, M1.get(i, 0));
		}
		for (int i = 0; i < B.getRowDimension(); i++) {
			We.set(i, i, B.get(i, 0));
		}
		calculate_Pv();
		U = Pv.transpose().times(U).times(a).plus(E1.times(1 - a));

	}

	public String getSummaryAndKeywords(int n, int m) {
		sortHyperedges();
		sortVertexs();
		StringBuffer buff = new StringBuffer();
/*
		buff.append("<关键词>：");

		for (Map.Entry<String, Double> e : sortedVertexsList) {
			if (e.getKey().length() < 2) {
				continue;
			}
			if (m-- > 0)
				buff.append(e.getKey() + " ");
		}
		buff.append("\n\n<摘要>:\n    ");*/
		List<String> list = new ArrayList<String>();
		int sumLength = 0;
		for (Map.Entry<String, Double> e : sortedEdgesList) {// 输出排序后的句子到文件
			sumLength += e.getKey().length();
			if (sumLength > n) {
				break;
			}
			list.add(e.getKey());
		}
		for (String s : hyperedge_list) {
			if (list.contains(s) && !buff.toString().contains(s))
				buff.append(s + "。");
		}
		
		
		return buff.toString();
	}

	public void sortHyperedges() {
		Map<String, Double> edgeScoreMap = new HashMap<String, Double>();
		for (int i = 0; i < V.getRowDimension(); i++) {
			edgeScoreMap.put(hyperedge_list.get(i), V.get(i, 0));
		}

		sortedEdgesList = new ArrayList<Map.Entry<String, Double>>(
				edgeScoreMap.entrySet());
		// 通过Collections.sort(List I,Comparator c)方法进行排序
		Collections.sort(sortedEdgesList,
				new Comparator<Map.Entry<String, Double>>() {

					@Override
					public int compare(Entry<String, Double> o1,
							Entry<String, Double> o2) {
						if (o1.getValue() >= o2.getValue()) {
							return -1;
						} else {
							return 1;
						}
					}
				});
	}

	public void sortVertexs() {
		Map<String, Double> vertexsScoreMap = new HashMap<String, Double>();
		Iterator<String> iter = vertex_set.iterator();
		for (int i = 0; i < U.getRowDimension(); i++) {
			vertexsScoreMap.put(iter.next(), U.get(i, 0));
		}

		sortedVertexsList = new ArrayList<Map.Entry<String, Double>>(
				vertexsScoreMap.entrySet());
		// 通过Collections.sort(List I,Comparator c)方法进行排序
		Collections.sort(sortedVertexsList,
				new Comparator<Map.Entry<String, Double>>() {

					@Override
					public int compare(Entry<String, Double> o1,
							Entry<String, Double> o2) {
						if (o1.getValue() >= o2.getValue()) {
							return -1;
						} else {
							return 1;
						}
					}
				});
	}
	public static String getSummary(String text,int length){
		HyperGraph graph= new HyperGraph(text);
		graph.testIterative();
		String summary=graph.getSummaryAndKeywords(length, 5);
		return summary;
	}
	public static void main(String[] args) {
		String text="　产业界有句名言：一流企业卖标准，二流企业卖品牌，三流企业卖产品；与此相关的一种观点认为竞争有三个层次：第一个层次是价格和质量的竞争，第二个层次是专利技术的竞争，第三个层次是标准和制度的竞争。如今，深圳不少企业已进入更高层次的竞争，一流的企业不断涌现，这在今年的深圳市标准创新奖和科技创新奖中可以清楚看到。深圳的自主创新，有了许多新气象。 　　政府创新奖，不仅是对创新的奖励和扶持，也是对创新的评估和引导。今年这一活动中颇引人注目的地方，就是创新主体更加多元，创新层次更加丰富，显示深圳创新型城市建设取得长足进步。 　　今年的“英雄榜”上，不仅有老大哥级的产业巨头，也有许多中小创新型企业，还有许多公共服务单位。华为、中兴等大企业，虽是老面孔，可今年带来了更多新成果，不少都是国际级的；众多创新型中小企业的上榜让人高兴，看到他们以各自的创新“绝活”登榜，才晓得为什么他们能在市场上胜人一筹。深圳之所以有创新之城的光彩，正是因为有许许多多的创新型企业和创新型人才在发光发热。 　　创新无处不在！与创新主体多元化相应的是，深圳创新活动在深度和广度上有了很大的进步。在科技创新方面，创新活动不但覆盖了电子、精密仪器等先进制造业，也涉及生物医药、新能源、新材料等新兴产业，还有金融、物流等现代服务业，这既与国际新一轮产业升级和科技创新趋势相适应，也与我国改善民生、促进内需等战略转型有密切联系，显示出深圳创新领域的勃勃生机。在标准创新方面，科技创新政策与标准化战略相结合结出硕果，自从我市2006年在全国率先实施城市标准化战略以来，政府有关部门通过资助标准化活动、设立标准创新奖、启动标准化工程师制度等办法，鼓励更多的企事业单位将技术优势转化为标准优势，不但增强了企业自主创新能力，而且有助于我们争夺国际标准制定的主导权，对增强产业竞争优势，提升国际竞争力，加快建设国际化城市和国家自主创新型城市都有重要意义。 　　创新以人为本，这在标准创新奖和科技创新奖得到了体现。一是对创新型人才的充分尊重。创新活动的关键要素是人才。鼓励创新，最终要把奖励落实到人，才能起到最大效果。在这次科技创新奖中，许多奖项都列出了创新人员的名字，他们是新时代的英雄。这许许多多名字，让我们体会到每一项重大创新成果的背后，是一个个具体的、鲜活的个人。正是他们的艰苦努力，使这些让许多人受益、推动社会进步的创新成果变成现实。他们理应得到全社会的尊重。二是创新造福人和社会。这次受到表彰的创新内容丰富，有些与百姓生活息息相关，像疾病防控、蔬菜质量监测、郊野公园规划等，都与百姓关注的医疗保障、食品安全等热点有关；像劳务工医保、濒危物种保护、行政数据交换格式等，则有利于规范行政管理，提高公共服务水平，对社会整体的福利提升都有重要作用。 　　给创新以更多的奖励，将激发全社会的创新活力。";
		//String text="12月7日下午，我校与中科招商集团共建中科众创学院签约仪式暨武汉市青桐学院授牌仪式在逸夫国际会议中心举行。中科招商集团联席总裁刘继军、高级副总裁刘海光、武汉市科技局副局长王振旭、我校校长杨宗凯、副校长蔡红生、校党委副书记覃红、副校长彭南生以及相关职能部门负责人、创业导师代表、创团团队代表等出席。会议由我校副校长蔡红生主持。覃红在致辞中提到创新教育的重要性，他强调，青年是创新创业的生力军，在广大青年中培育创业精神，开展创业活动对学生成长成才、对经济发展转型，成就国家梦想具有重要意义。同时，武汉市加大步伐支持大学生创新创业活动，华师也注重学生的个性化培养，为大学生创业提供了专业化的指导。他也提到了学校在创业实践上的上的相关问题，他表示，在校企合作中应注重创业教育培训与指导，开发创业课程、教材体系，对接企业，加强个性化创业辅导和实施不同的奖励制度，成立创业资金等。刘继军首先阐述，创业教育应该由企业和高校结合。经济上的回报、社会成就感和未来的发展空间是公司考虑的问题。随着中国的高校的创业率的增加，创新型国家的建设转型，企业和高校合作创新创业教育更加重要。他指出，国外的基础教育已经融入了创业理念，透过与华师的合作，今后在创业师资的培养上影响会更加深远。他也表明，公司与多所院校合作，也达到院校间协同育人、优势互补的目的。杨宗凯和刘继军签署华中师范大学中科众创学院战略合作协议，随后武汉市科技局副局长王振旭向我校授青桐学院牌，彭南生副校长接牌。座谈环节，华师校友兼创业导师、颂大教育集团总经理丁卫华指出更快更好在创业中少走弯路、尽可能去做社会的引领，规范的引领创业大学生创业。另外，创业前期做风险控制时需要采取措施帮助创业者规避风险，充分发挥学校与中科招商集团合作的优势。会后，与会代表集体合影留念，此次会议的圆满结束标志着我校大学生创新创业工作上了新的台阶，具有里程碑的意义。";
		String summary=getSummary(text, 200);
		System.out.println(summary);
		/*System.out.println( StandardTokenizer.segment("产业界有句名言：一流企业卖标准，二流企业卖品牌，"));*/
		
	}

}
