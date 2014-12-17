/**
 * Usage:
 * config = require(./config.js);
 *
 * Then you can use the constants defined here.
 */
module.exports = {
	
	/*config Sparkserver*/
	sparkServerHOST: 'localhost',
	sparkServerPORT: 9000,
	sparkPrefixPath: 'hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/',

	/*config BrowserClient*/
	browserClientPort: 3001,

	/*config SQL Database*/
	sqlDatabaseHost: 'db.f4.htw-berlin.de',
	sqlDatabasePort: 3306,
	sqlDatabaseUser: 's0536746',
	sqlDatabasePassword: '123xyz,.',
	sqlDatabase: '_s0540031__Twitter',
	sqlDatabaseTimezone: '+0000'
};