package nl.cwi.monetdb.TPCH;

import nl.cwi.monetdb.Benchmarks.*;
import nl.cwi.monetdb.Populate.H2Populate;
import nl.cwi.monetdb.Populate.MonetDBLitePopulate;
import nl.cwi.monetdb.Populate.TPCHPopulate;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

public class TPCHMain {

	public static void displayError(String message) {
		System.err.println(message);
		System.exit(1);
	}

	private static void displayHelp() {
		System.out.println("Usage of JVM TPC-H benchmark tool");
		System.out.println("Parameters: { populate | evaluate | help } other_parameters");
		System.out.println("For populate: { MonetDBLite-Java | H2 } scale_factor database_path import_path");
		System.out.println("For evaluate: { MonetDBLite-Java | H2 } scale_factor database_path query_path");
		System.exit(0);
	}

	private static void populate(String[] args) throws Exception {
		TPCHPopulate popMe = null;

		if(args.length < 5) {
			displayError("Usage: populate { MonetDBLite-Java | H2 } scale_factor database_path import_path");
			return;
		}
		switch (args[1]) {
			case "MonetDBLite-Java":
				popMe = new MonetDBLitePopulate();
				break;
			case "H2":
				popMe = new H2Populate();
				break;
			default:
				displayError("Database " + args[1] + " is not supported");
				break;
		}
		if(popMe != null){
			popMe.populate(args[3], args[4]);
		}
	}

	private static void validatePaths(String newDatabasePath, String newQueryPath) {
		File databaseDirectory = new File(newDatabasePath);
		if(!databaseDirectory.isAbsolute()) {
			TPCHMain.displayError(newDatabasePath + " is not a absolute path");
		}
		if(!databaseDirectory.exists()) {
			TPCHMain.displayError("Directory " + newDatabasePath + " does not exist");
		}
		if(!databaseDirectory.isDirectory()) {
			TPCHMain.displayError(newDatabasePath + " is not a directory");
		}

		File queryDirectory = new File(newQueryPath);
		if(!queryDirectory.isAbsolute()) {
			TPCHMain.displayError(newQueryPath + " is not a absolute path");
		}
		if(!queryDirectory.exists()) {
			TPCHMain.displayError("Directory " + newQueryPath + " does not exist");
		}
		if(!queryDirectory.isDirectory()) {
			TPCHMain.displayError(newQueryPath + " is not a directory");
		}
	}

	private static void evaluate(String[] args) throws RunnerException {
		if(args.length < 5) {
			displayError("Usage: evaluate { MonetDBLite-Java | H2 } scale_factor database_path query_path");
			return;
		}
		switch (args[1]) {
			case "MonetDBLite-Java":
				break;
			case "H2":
				break;
			default:
				displayError("Database " + args[1] + " is not supported");
				break;
		}

		validatePaths(args[3], args[4]);

		Options opt = new OptionsBuilder()
			.include(TPCHBenchmark.class.getSimpleName())
			.timeUnit(TimeUnit.MILLISECONDS)
			.mode(Mode.AverageTime)
			.threads(1)
			.forks(1)
			.warmupIterations(1)
			.measurementIterations(3)
			.param("databaseSystem", args[1])
			.param("scaleFactor", args[2])
			.param("databasePath", args[3])
			.param("queryPath", args[4])
			.jvm(args[5])
			.build();

		//Hack to find org.openjdk.jmh.runner.ForkedMain class, because TPCHMain is called from maven exec plugin
		//https://stackoverflow.com/questions/35574688/how-to-run-a-jmh-benchmark-in-maven-using-execjava-instead-of-execexec
		URLClassLoader classLoader = (URLClassLoader) nl.cwi.monetdb.Benchmarks.TPCHBenchmark.class.getClassLoader();
		StringBuilder classpath = new StringBuilder();
		for(URL url : classLoader.getURLs())
			classpath.append(url.getPath()).append(File.pathSeparator);
		System.setProperty("java.class.path", classpath.toString());
		System.setProperty("java.vm.name", "Maxine!!!");

		new Runner(opt).run();
	}

	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			displayError("The activity parameter must be provided: { populate | evaluate | help }");
			return;
		}
		switch (args[0]) {
			case "help":
				displayHelp();
				break;
			case "populate":
				populate(args);
				break;
			case "evaluate":
				evaluate(args);
				break;
			default:
				displayError("The activity parameter must be { populate | evaluate | help }");
				break;
		}
	}
}
