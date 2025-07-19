FROM mysql:8.0
RUN chown mysql:mysql /var/run/mysqld && chmod 750 /var/run/mysqld