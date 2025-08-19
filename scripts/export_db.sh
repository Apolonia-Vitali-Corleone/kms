#!/bin/bash
mysqldump -u root -p --databases kms --routines --events --triggers > kms.sql
