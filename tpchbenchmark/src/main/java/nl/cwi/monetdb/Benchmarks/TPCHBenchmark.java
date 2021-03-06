package nl.cwi.monetdb.Benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class TPCHBenchmark {

	//Please override the parameters always
	@Param({"MonetDBLite-Java"})
	public String databaseSystem;

	@Param({"1"})
	public String scaleFactor;

	@Param({"/tmp/database"})
	public String databasePath;

	@Param({"/tmp/query"})
	public String queryPath;

	private TPCHSetting benchSetting;

	@Setup(Level.Trial)
	public void setupTPCH() throws Exception {
		float sf;
		switch (this.databaseSystem) {
			case "MonetDBLite-Java":
				this.benchSetting = new MonetDBLiteJavaSetting();
				break;
			case "H2":
				this.benchSetting = new H2Setting();
				break;
			default:
				System.err.println("Database " + this.databaseSystem + " is not supported");
				System.exit(1);
				break;
		}
		try {
			sf = Float.parseFloat(this.scaleFactor);
		} catch (Exception ex) {
			throw new NumberFormatException("Scale factor is not a valid number");
		}
		if(sf < 0) {
			throw new NumberFormatException("Scale factor cannot be negative");
		}
		this.benchSetting.setupQueries(this.queryPath, sf);
	}

	@Setup(Level.Iteration) //create a new connection for every iteration
	public void openConnection() throws Exception {
		this.benchSetting.setupConnection(this.databasePath);
	}

	@TearDown(Level.Iteration)
	public void closeConnection() throws Exception {
		this.benchSetting.closeConnection();
	}

	@Benchmark
	public void tpch01(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch01(blackhole);
	}

	@Benchmark
	public void tpch02(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch02(blackhole);
	}

	@Benchmark
	public void tpch03(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch03(blackhole);
	}

	@Benchmark
	public void tpch04(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch04(blackhole);
	}

	@Benchmark
	public void tpch05(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch05(blackhole);
	}

	@Benchmark
	public void tpch06(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch06(blackhole);
	}

	@Benchmark
	public void tpch07(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch07(blackhole);
	}

	@Benchmark
	public void tpch08(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch08(blackhole);
	}

	@Benchmark
	public void tpch09(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch09(blackhole);
	}

	@Benchmark
	public void tpch10(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch10(blackhole);
	}

	@Benchmark
	public void tpch11(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch11(blackhole);
	}

	@Benchmark
	public void tpch12(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch12(blackhole);
	}

	@Benchmark
	public void tpch13(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch13(blackhole);
	}

	@Benchmark
	public void tpch14(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch14(blackhole);
	}

	@Benchmark
	public void tpch15(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch15(blackhole);
	}

	@Benchmark
	public void tpch16(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch16(blackhole);
	}

	@Benchmark
	public void tpch17(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch17(blackhole);
	}

	@Benchmark
	public void tpch18(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch18(blackhole);
	}

	@Benchmark
	public void tpch19(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch19(blackhole);
	}

	@Benchmark
	public void tpch20(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch20(blackhole);
	}

	@Benchmark
	public void tpch21(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch21(blackhole);
	}

	@Benchmark
	public void tpch22(Blackhole blackhole) throws Exception {
		this.benchSetting.tpch22(blackhole);
	}
}
