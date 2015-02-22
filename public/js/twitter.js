window.twttr = (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0],
        t = window.twttr || {};
    if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
    js.src = "https://platform.twitter.com/widgets.js";
    fjs.parentNode.insertBefore(js, fjs);

    t._e = [];
    t.ready = function(f) {
        t._e.push(f);
    };

    return t;
}(document, "script", "twitter-wjs"));

twitter = {
    /**
     * Draws a the embeeded Tweets
     */
    createTweets: function(tweetIds, divElement){
        var j = 0;
        var tweets = new Array();
        for(var i = 0; i < tweetIds.length; i++){
            if(tweetIds[i] == ","){
                j++
            } else {
                if(typeof tweets[j] != 'undefined'){
                    tweets[j] = tweets[j] + tweetIds[i];
                } else {
                    tweets[j] = tweetIds[i];
                }

            }
        }
        for(var i = 0; i < tweets.length; i++){
            twttr.widgets.createTweet(
                tweets[i],
                document.getElementById(divElement + i),
                {
                    align: 'left'
                });
        }
    }
};