#!/bin/bash
/usr/local/mysql/bin/mysql -u admin metainformacion -p < bdMetainformacion.sql

/usr/local/mysql/bin/mysql -u admin ficheros -p < bdFicheros.sql