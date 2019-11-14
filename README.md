# tpch-monetdblite
Run TPC-H benchmarks on MonetDBLite and other databases for the JVM.

## Dependencies:
- Cmake (used by tpch-dbgen)
- OpenJDK 1.8
- Apache Maven (must be set on `$PATH` environment variable)
- GNU Make (used by tpch-dbgen)
- GCC compiler
- GNU Core Utilities
- An Unix shell (I use bash)

## How to run

Don't forget to init the repository's submodule : `git clone --recurse-submodules ...` or after cloning, 
`git submodule update --init --recursive`.

At the same time, I'm using a SNAPSHOT
release for MonetDBLite-Java, and Sonatype's snapshot repository is not enabled by default. The 
`allow-snapshots` repository enables it.

On the `settings.xml` file of Apache Maven add the two profiles:

    <profile>
      <id>allow-snapshots</id>
      <activation><activeByDefault>true</activeByDefault></activation>
      <repositories>
        <repository>
          <id>snapshots-repo</id>
          <url>https://oss.sonatype.org/content/repositories/snapshots</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </repository>
      </repositories>
    </profile>

Also set them to active under the active profiles tab:

    <activeProfile>allow-snapshots</activeProfile>

The `$JVM_TO_EVAL` environment variable, must be set with the path to the `java` command of the JVM to benchmark, e.g.

```sh
export JVM_TO_EVAL=/opt/code/maxine-src/maxine/maxjdk/bin/java
```

On the cloned tpch-monetdblite directory the following scripts are available:

- `generate_dataset.sh` for importing:
```sh
./generate_dataset.sh --sf 1 --database { MonetDBLite-Java | H2 } --path <Absolute path of MonetDBLite-Java database farm>
```

- `run_tpch_queries.sh` for TPC-H benchmark run:
```sh
./run_tpch_queries.sh --sf 1 --database { MonetDBLite-Java | H2 } --path  <Absolute path of MonetDBLite-Java database farm>
```
