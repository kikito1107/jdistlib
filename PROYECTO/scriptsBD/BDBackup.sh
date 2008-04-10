#!/bin/bash
/usr/local/mysql/bin/mysqldump -u root metainformacion > ../bdMetainformacion.sql
/usr/local/mysql/bin/mysqldump -u root ficheros > ../bdFicheros.sql
