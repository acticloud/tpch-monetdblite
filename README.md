# tpch-monetdblite
Run TPC-H benchmarks on MonetDBLite

For importing do:
./generate_dataset.sh --sf 1 --database MonetDBLite-Java --path <Absolute path of MonetDBLite-Java database farm>

For benchmark execution do:
./run_tpch_queries.sh --sf 1 --database MonetDBLite-Java --path  <Absolute path of MonetDBLite-Java database farm>
