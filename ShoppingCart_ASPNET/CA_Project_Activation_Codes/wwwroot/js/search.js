window.onload = function () {
    searchBtn();
    searchTB();

}
//console.log(window.location.href);

function searchTB() {
    search_elem = document.getElementById("searchStr");
    search_elem.addEventListener("keypress", function (event) {
        if (event.key == "Enter") {
            search();

        }
    });
}

function searchBtn() {
    searchBtn_elem = document.getElementById("searchBtn");
    searchBtn_elem.addEventListener("click", function (event) {
        search();
    });
}
function search() {
    var searchStr = document.getElementById("searchStr").value.trim();

    // Get the current URL
    var currentUrl = window.location.href;

    // Check if the search string is not empty
    if (searchStr.length !== 0) {
        var newUrl;
        var baseUrl;

        if (currentUrl.includes("/Gallery/Index")) {
            newUrl = currentUrl.split("?")[0] + "?searchStr=" + encodeURIComponent(searchStr);
        } else if (currentUrl.includes("/Gallery")) {
            newUrl = currentUrl.split("/Gallery")[0] + "/Gallery/Index?searchStr=" + encodeURIComponent(searchStr);
        } else {
            newUrl = "/Gallery/Index?searchStr=" + encodeURIComponent(searchStr);
        }

        // Redirect to the new URL
        window.location.href = newUrl;
    }
    else {
        window.location.href = "/Gallery"
        //window.location.replace("https://localhost:7037");
    }
}



