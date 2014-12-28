/**
 * Usage:
 * config = require(./config.js);
 *
 * Then you can use the constants defined here.
 */
module.exports = {
	
	/*config Sparkserver*/
	sparkServerHOST: 'hadoop03.f4.htw-berlin.de',
	sparkServerPORT: 5555,
	//sparkPrefixPath: 'hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/',

	/*config BrowserClient*/
	browserClientPort: 8080,

	/*config SQL Database*/
	sqlDatabaseHost: 'db.f4.htw-berlin.de',
	sqlDatabasePort: 3306,
	sqlDatabaseUser: 's0536746',
	sqlDatabasePassword: '123xyz,.',
	sqlDatabase: '_s0540031__Twitter',
	sqlDatabaseTimezone: '+0000',
	sqlDatabaseCharset: 'utf8_general_ci',
	// database limits count of connects per user to 10
	sqlConnectionLimit: 7
};