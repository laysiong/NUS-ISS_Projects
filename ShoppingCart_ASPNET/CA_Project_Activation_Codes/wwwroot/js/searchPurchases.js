
window.onload = function () {
    searchTBHistory();
    searchBtnHistory();
    //console.log("i am in")
}
function searchTBHistory() {
    search_elem = document.getElementById("searchStr");

    //console.log("searching - enter")

    search_elem.addEventListener("keypress", function (event) {
        if (event.key == "Enter") {
            searchHistory();

        }
    });
}

function searchBtnHistory() {
    searchBtn_elem = document.getElementById("searchBtn");

    //console.log("searching - search but")

    searchBtn_elem.addEventListener("click", function (event) {
        searchHistory();
    });
}

function searchHistory() {
    var searchStr = document.getElementById("searchStr").value.trim();

    // Get the current URL
    var currentUrl = window.location.href;

    // Check if the search string is not empty
    if (searchStr.length !== 0) {
        var newUrl;

        // Check if the current URL already contains "/Gallery/Index"
        if (currentUrl.includes("/Purchases/Index")) {
            newUrl = currentUrl.split("?")[0] + "?searchStr=" + encodeURIComponent(searchStr);
        } else if (currentUrl.includes("/Purchases")) {
            newUrl = currentUrl.split("/Purchases")[0] + "/Purchases/Index?searchStr=" + encodeURIComponent(searchStr);
        } else {
            newUrl = "/Purchases/Index?searchStr=" + encodeURIComponent(searchStr);
        }

        // Redirect to the new URL
        window.location.href = newUrl;
    } else {

        window.location.href = "/Purchases";
    }
}
