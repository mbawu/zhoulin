package xinyuan.cn.zhoulin.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String NAME = "Province";

	public MySQLiteOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	/** 通过21行的输出语句可知，onCreate方法只会执行一次 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS Province (_id integer primary key autoincrement, area text)");
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 0, "请选择地区" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 1,
						"安庆市,蚌埠市,亳州市,巢湖市,池州市,滁州市,阜阳市,合肥市,淮北市,淮南市,黄山市,六安市,马鞍山市,宿州市,铜陵市,芜湖市,宣城市" });
		System.out.println("SQLiteDatabase------------------------");
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 2,
						"昌平区,朝阳区,大兴区,房山区,海淀区,怀柔区,门头沟区,密云县,平谷区,石景山区,顺义区,通州区,西城区,宣武区,延庆县" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] {
						3,
						"巴南区 ,北碚区,长寿区,达州市,涪陵区,江北区,九龙坡区,南岸区,黔江区,沙坪坝区,双桥区,万盛区,万州区,渝北区,渝中区,璧山县,城口县,大足县,垫江县,丰都县,奉节县,合川市,江津市,开县,梁平县,南川市,彭水苗族土家族自治县,荣昌县,石柱土家族自治县,铜梁县,巫山县,巫溪县,武隆县,秀山土家族苗族自治县,永川市,酉阳土家族苗族自治县,云阳县,忠县" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 4,
						"城厢区,福州市,涵江区,荔城区,龙岩市,南平市,宁德市,莆田市,泉州市,三明市,秀屿区,秀屿区,漳州市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 5,
						"白银市,定西市,甘南州,海南州,金昌市,酒泉市,兰州市,临夏州,陇南市,平凉市,庆阳市,天水市,武威市,张掖市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] {
						6,
						"潮州市,东莞市,佛山市,广州市,河源市,惠州市,江门市,揭阳市,茂名市,梅州市,清远市,汕头市,汕尾市,韶关市,深圳市,阳江市,云浮市,湛江市,肇庆市,中山市,珠海市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 7,
						"百色市,北海市,,防城港市,贵港市,桂林市,河池市,贺州市,来宾市,柳州市,南宁市,钦州市,梧州市,玉林市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 8,
						"安顺市,毕节地区,贵阳市,六盘水市,黔东南苗族侗族自治州,黔南布依族苗族自治州,黔西南布依族苗族自治州,铜仁地区,遵义市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 9, "海口市,海南沿革,三亚市,大渡口区" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 10,
						"保定市,沧州市,承德市,邯郸市,衡水市,廊坊市,秦皇岛市,石家庄市,唐山市,邢台市,张家口市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 11,
						"安阳市,鹤壁市,焦作市,开封市,洛阳市,漯河市,南阳市,平顶山市,濮阳市,三门峡市,商丘市,新乡市,信阳市,许昌市,郑州市,周口市,驻马店市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 12,
						"大庆市,大兴安岭地区,哈尔滨市,鹤岗市,黑河市,鸡西市,佳木斯市,牡丹江市,七台河市,齐齐哈尔市,双鸭山市,绥化市,伊春市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 13,
						"鄂州市,恩施土家族苗族自治州,黄冈市,黄石市,荆门市,荆州市,十堰市,随州市,武汉市,咸宁市,襄樊市,孝感市,宜昌市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 14,
						"长沙市,常德市,郴州市,衡阳市,怀化市,娄底市,邵阳市,天门市,湘潭市,湘西土家族苗族自治州,益阳市,永州市,岳阳市,张家界市,株洲市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 15, "白城市,白山市,长春市,吉林市,辽源市,四平市,松原市,通化市,延边朝鲜族自治州" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 16,
						"常州市,淮安市,连云港市,南京市,南通市,苏州市,宿迁市,泰州市,无锡市,徐州市,盐城市,扬州市,镇江市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 17,
						"抚州市,赣州市,吉安市,景德镇市,九江市,南昌市,萍乡市,上饶市,新余市,宜春市,鹰潭市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 18,
						"鞍山市,本溪市,朝阳市,大连市,丹东市,抚顺市,阜新市,葫芦岛市,锦州市,辽阳市,盘锦市,沈阳市,铁岭市,营口市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 19,
						"阿拉善盟,巴彦淖尔市,包头市,赤峰市,鄂尔多斯市,呼和浩特市,呼伦贝尔市,通辽市,乌海市,乌兰察布市,锡林郭勒盟,兴安盟" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 20, "固原市,石嘴山市,吴忠市,博尔塔拉州,中卫市,银川市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 21, "果洛州,海北州,海东地区,银川市,海西州,黄南州,西宁市,玉树州" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 22,
						"滨州市,德州市,东营市,菏泽市,济南市,济宁市,莱芜市,聊城市,临沂市,青岛市,日照市,泰安市,威海市,潍坊市,烟台市,枣庄市,淄博市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 23,
						"长治市,大宁县,大同市,晋城市,晋中市,临汾市,吕梁市,朔州市,太原市,忻州市,阳泉市,运城市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 24, "安康市,宝鸡市,汉中市,嘉峪关市,铜川市,渭南市,西安市,咸阳市,延安市,榆林市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 25,
						"宝山区,长宁区,崇明县,奉贤区,虹口区,黄浦区,嘉定区,金山区,静安区,卢湾区,闵行区,南汇区,浦东新区,普陀区,青浦区,松江区,徐汇区,杨浦区,闸北区" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] {
						26,
						"阿坝藏族羌族自治州,巴中市,成都市,六盘水市,德阳市,甘孜藏族自治州,广安市,广元市,乐山市,凉山彝族自治州,泸州市,眉山市,绵阳市,内江市,南充市,攀枝花市,遂宁市,雅安市,宜宾市,资阳市,自贡市" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 27,
						"和平区,河东区,河西区,南开区,河北区,红桥区,塘沽区,汉沽区,大港区,东丽区,西青区,津南区,北辰区,武清区,宝坻区,宁河县,静海县,蓟县" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 28, "阿里地区,昌都地区,拉萨市,商洛市,那曲地区,日喀则地区,山南地区" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 29,
						"阿克苏地区,阿勒泰地区,巴音郭楞州,观塘区,昌吉州,哈密地区,和田地区,喀什地区,克拉玛依市,克孜勒苏州,塔城地区,吐鲁番地区,乌鲁木齐市,伊犁州" });
		db.execSQL(
				"INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] {
						30,
						"保山市,楚雄彝族自治州,大理白族自治州,德宏傣族景颇族自治州,迪庆藏族自治州,红河哈尼族彝族自治州,昆明市,丽江市,临沧市,怒江傈傈族自治州,曲靖市,思茅市,文山壮族苗族自治州,西双版纳傣族自治州,玉溪市,昭通市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 31,
						"杭州市,湖州市,嘉兴市,金华市,丽水市,宁波市,衢州市,绍兴市,台州市,温州市,舟山市" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 32, "台湾" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 33, "香港" });
		db.execSQL("INSERT INTO Province (_id, area) VALUES(?, ?)",
				new Object[] { 34, "澳门" });
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
