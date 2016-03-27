#!/bin/bash

cmd="gzip -9"

echo "Compressing man file astrojournal.1 and changelog with $cmd"
$cmd astrojournal.1
$cmd changelog
