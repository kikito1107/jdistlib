#!/bin/bash
/usr/local/mysql/bin/mysql -u root < crearBD
/usr/local/mysql/bin/mysql -u admin metainformacion -p < bdMetainformacion.sql
