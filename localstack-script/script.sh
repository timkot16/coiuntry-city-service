#!/bin/bash
awslocal s3api create-bucket \
--bucket city-logo-bucket \
--create-bucket-configuration LocationConstraint=eu-central-1
