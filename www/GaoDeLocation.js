var exec = require('cordova/exec');

exports.getCurrentPosition = function(success, error) {
	exec(success, error, 'GaoDeLocation', 'getLocation', []);
};

// var GaoDeLocation = {
// 	getCurrentPosition:function (successFn, errorFn) {
// 		exec(successFn, errorFn, 'GaoDeLocation', 'getLocation', []);
// 	}
// };
// module.exports = GaoDeLocation;