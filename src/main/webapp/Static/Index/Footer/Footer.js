
githubLinkBoxP = $(".github_link_box p")[0];

function addEvent() {
	githubLinkBoxP.addEventListener("click", () => {
		location.href = "https://github.com/SeDan2004/DomMebeli";
	})
}

addEvent();