#!/bin/bash

###
# #%L
# BH2K Portal Documentation
# %%
# Copyright (C) 2015 - 2016 Blockhaus2000
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###

TEX_ARGS="-interaction batchmode"

compile() {
	pdflatex $TEX_ARGS $1
}

print_help() {
	echo "Usage: $0 <file> [compile_count (default 5)]" >&2
}

regfile="$1"
texfile="$regfile.tex"
file=""
if [[ "$regfile" == *".tex" && -f "$regfile" ]]; then
	file="$regfile"
elif [[ -f "$texfile" ]]; then
	file="$texfile"
else
	echo "$regfile does not exist or is not a LaTeX file and $texfile does not exist!" >&2
	print_help

	exit 126
fi
shift

count="5"
if [[ "$1" != "" ]]; then
	count="$1"
fi

echo "Compiling $file with the tex arguments '$TEX_ARGS' $count time$( [[ "$count" != "1" ]] && echo "s" )."

for i in $(seq 1 $count); do
	number="$1"
	if [[ "$i" == "1" ]]; then
		number="${i}st"
	elif [[ "$i" == "2" ]]; then
		number="${i}nd"
	elif [[ "$i" == "3" ]]; then
		number="${i}rd"
	else
		number="${i}th"
	fi

	echo -e "\n\n$number Compilation:\n"
	compile $file
done

