var captureScreenshots = null;
var elements = null;
var currIndex = 0;

var myDevice = null;
var viewPort = { x :20, y: 20, width: 320, height: 480};

function Device() {
    
    this.viewPort = $('<div>').css({
        position : 'absolute',
        top : viewPort.x,
        left : viewPort.y,
        width : viewPort.width,
        height : viewPort.height,
        overflow : 'hidden',
        padding: 0,
        margin: 0,
        border: 'none'
    }).get(0);
    
    this.el = $('<div>')
            .css({
                padding:0,
                margin:0,
                border: 'none',
                position : 'absolute',
                width : 640,
                height : 960,
                overflow : 'hidden',
                top : 0,
                left : 0
            })
            .get(0);
    this.scrollX = 0;
    this.scrollY = 0;
    
    var children = [];
    $('body').children().each(function() { children.push(this);}).remove();
    var self = this;
    $(children).each(function(k,v){ 
        $(self.el).append(v);
    });
    $(this.viewPort).append(this.el);
    $('body').append(this.viewPort);
    
}

Device.prototype.scrollTo = function(x,y) {
    window.app.log("Scrolling to "+x+','+y);
    $(this.el).css({
        left : -x,
        top :  -y
    });
    this.scrollX = x;
    this.scrollY = y;
};

$(document).ready(function() {
    //window.app.log("We are in ready");
    myDevice = new Device();
    window.captureScreenshots = function() {
        window.app.log("in captureScreenshots");
        if (window.elements === null) {
            $('.element').hide();
            window.elements = [];
            $('.element').each(function() {
                window.elements.push(this);
            });
        }
        if (currIndex >= elements.length) {
            window.app.finishedCaptureScreenshotsCallback();
            return;
        }
        $('.element').hide();
        var currEl = elements[currIndex++];
        $(currEl).show();
        myDevice.scrollTo(0,0);
        setTimeout(function() {
            var rect = currEl.getBoundingClientRect();
            setTimeout(function() {
                // This passes back actual coordinates in the real webview viewport - not the virtual viewport
                window.app.log("Element is located at "+rect.left+", "+rect.top+", "+rect.width+", "+rect.height);
                window.app.createScreenshotCallback($(currEl).attr('id'), rect.left, rect.top, rect.width, rect.height);
            }, 2);
        }, 50);
        
        
    };
    
});

function getRectSnapshot(x, y, w, h) {
    window.snapper.log("Hello");
    myDevice.scrollTo(x-20, y-20);
    setTimeout(function() {
        window.snapper.log("About to call handleJSSnap()");
        window.snapper.handleJSSnap(window.scrollX, window.scrollY, x, y, w, h);
    }, 50);
}



