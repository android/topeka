// phantomjs generate_pngs.js input.svg output.png 64x64

var page = require('webpage').create();
var system = require('system');

if (system.args.length < 4) {
    console.log('Too few arguments.');
    phantom.exit(1);
}

var input = system.args[1];
var output = system.args[2];
var size = system.args[3].split('x').map(function (v) { return parseInt(v); } );

page.viewportSize = { width: size[0], height: size[1] };
page.clipRect = { top: 0, left: 0, width: size[0], height: size[1] };
page.open(input, function (status) {
    if ('success' !== status) {
	console.log ('Cannot open: ' + input);
	phantom.exit(1);
	return;
    }
    window.setTimeout(function () {
	page.render(output);
	phantom.exit();
    }, 200);
});
