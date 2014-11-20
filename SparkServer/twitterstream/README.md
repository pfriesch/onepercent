Enthält die Configuration des Flume Clients inklusive der Twitter Custom Source.

Um den FLume Agent im Hintergrund zu starten muss das flume-ng-agent.sh Skript an den auzuführenden Nutzer angepasst werden.

Crontab
*/20 * * * * /home/05/40031/apache-flume/flume-ng-agent.sh start > /dev/null
@reboot /home/05/40031/apache-flume/flume-ng-agent.sh start > /dev/null
@daily /home/05/40031/apache-flume/cleanup.sh 28
