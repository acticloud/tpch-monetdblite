#!/usr/bin/env bash

# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0.  If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright 2017-2018 MonetDB Solutions B.V.

function is_positive_number {
	[[ $1 =~ ^[0-9]+\.?[0-9]*$ ]]
}

function abort_script {
	echo $1 >&2 # error message to stderr
	exit 1
}

function is_in_list {
	local needle="$1"
	shift
	for haystack_item in $@; do
		[[ "$needle" == "$haystack_item" ]] && found=1
	done
	[[ -n "$found" ]]
}

function mhelp {
	echo "Usage: $0 --database <MonetDBLite-Java | H2> --path <path> "
	echo "It's assumed that the database contains a TPC-H schema with an appropriate scale factor"
	echo "path should be absolute"
}

scale_factor=
database=
input_path=

while [ "$#" -gt 0 ]; do
	case "$1" in
		-s|--sf)
			scale_factor="$2"
			is_positive_number "$scale_factor" || abort_script "Invalid scale factor size $scale_factor"
			shift
			shift
			;;
		-d|--database)
			database="$2"
			shift
			shift
			;;
		-p|--path)
			input_path="$2"
			shift
			shift
			;;
		-h|--help)
			mhelp
			exit 0
			;;
		*)
			mhelp
			abort_script "$0: Unknown parameter $1"
			;;
	esac
done

[[ -n "$scale_factor" ]] || abort_script 'Scale factor not set'

[[ -n "$database" ]] || abort_script 'Database not set'

is_in_list "$database" 'MonetDBLite-Java' 'H2' || abort_script "Database $database not supported"

[[ -n "$input_path" ]] || abort_script 'Input path not set'

[[ -d "$input_path" ]] || abort_script "Path $input_path is not a directory"

# Compile TPC-H dbgen

tpch_gen_dir=$(dirname `realpath $0`)/tpch-tools/dbgen

[[ -d "$tpch_gen_dir" ]] || abort_script "$tpch_gen_dir directory not found (Did you forget to clone the git submodule?)"

if [[ ! -f "$tpch_gen_dir/qgen" ]]; then
	pushd "$tpch_gen_dir" >/dev/null

	type cmake 1>/dev/null || abort_script "CMake build tool unavailable"
	cmake -DMACHINE=LINUX -DDATABASE=VECTORWISE . || abort_script "Failure building the generation utility in $tpch_gen_dir during CMake"

	[[ -f "$tpch_gen_dir/Makefile" ]] || abort_script "Could not find or generate a Makefile in $tpch_gen_dir for building the generator utility"
	type make 1>/dev/null || abort_script "GNU Make unavailable"
	make -C "$tpch_gen_dir" || abort_script "Failure building the generation utility in $tpch_gen_dir - Make failure"
	[[ -f "$tpch_gen_dir/qgen" ]] || abort_script "Although the build of $tpch_gen_dir/qgen has supposedly succeeded - the binary is missing."

	popd > /dev/null
fi

# Run TPC-H benchmark

tpch_query_dir=$(dirname `realpath $0`)/tpch-queries/"$database"
tpch_benchmark_dir=$(dirname `realpath $0`)/tpchbenchmark

pushd "$tpch_benchmark_dir" >/dev/null

type mvn 1>/dev/null || abort_script "maven unavailable"

mvn package || abort_script "Error while compiling tpchbenchmark maven project"

mvn exec:java -Dexec.mainClass="nl.cwi.monetdb.TPCH.TPCHMain" -Dexec.args="evaluate $database $scale_factor $input_path $tpch_query_dir $JVM_TO_EVAL" || abort_script "Error while running TPC-H benchmark"

popd > /dev/null

echo "TPC-H benchmark scale factor $scale_factor successfully ran on $database database on $input_path directory"
