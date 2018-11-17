package logging;

public class LogTester {
	public static JSimpleLog log = new JSimpleLog();
	public static void main(String[] args) {
		log.setFormLog("foo1", true);
		log.setFormLog("foo2", false);
		log.categorize("foo", "foo1", "foo2");
		log.categorize("methods", "foo1", "foo2");
		log.setFormLog("methods", false);
		log.setFormLog("foo", false);
		foo1();
		foo2();
		foo3();
	}
	public static void foo1() {
		log.setType("foo1");
		foo2();
		foo2();
		foo3();
		foo2();
		foo3();
		log.out("foo1 is here!");
		log.reset();
	}
	public static void foo2() {
		log.setType("foo2");
		log.out("foo2 is here!");
		log.reset();
	}
	public static void foo3() {
		log.setType("foo");//set to a category
		log.out("foo3 is here!");
		log.reset();
	}
}
