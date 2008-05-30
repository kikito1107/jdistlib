if [ -e "./servicios.log" ]
then
	rm ./servicios.log
fi

java -Djava.security.policy=jsk-all.policy -jar ../lib/jini/lib/start.jar start-transient-jeri-services.config > ./servicios.log
