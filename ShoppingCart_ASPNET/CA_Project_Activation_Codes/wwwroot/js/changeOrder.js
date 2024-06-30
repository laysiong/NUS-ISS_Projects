var iconElem = document.getElementById("order_icon");
function switchIcon() {
    // once the div has been clicked
    var newUrl;
    var currentUrl = window.location.href;
    if (currentUrl.includes("/Purchases")) {
        if (currentUrl.includes("asc")) {
            newUrl = window.location.href.split("asc")[0] + encodeURIComponent("dsc");
        } else if (currentUrl.includes("dsc")) {
            newUrl = window.location.href.split("dsc")[0] + encodeURIComponent("asc");
        } else if (currentUrl.includes("?")) {
            // firs get into Purchases Page
            // the URL is like https://localhost:portnumber/Purchases
            // first click swith into ascending order
            newUrl = "/Purchases/Index?"+window.location.href.split("?")[1] + "&order=asc";
        } else {
            newUrl = "/Purchases/Index?order=asc";
        }
    }
    window.location.href = newUrl;
}