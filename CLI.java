public class CLI {
	static final String HELP = """
			CTP - Code To PlantUML

			Usage: ctp [lang] [path] [output]

			Available langs: java | cpp
			Path: a path to a folder inside the current directory
			Output: the name of resulting diagram file (generally in .puml)

			Examples:
			ctp cpp src diagram.puml
			ctp cpp . classdiagram.plantuml
			ctp java app/src/main/org/example/stack stack.puml
				""";

	public static void main(String args[]) {
		System.out.println(HELP);
	}
}
