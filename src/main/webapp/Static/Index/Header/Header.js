wrapper = $(".wrapper")[0];
[profileBtn, signInBtn, backetBtn] = $(".header_box2")[0].children;

function openAuthAndRegForm() {
	TweenMax.to(wrapper, 0.5, {opacity: 1, onStart: () => {
		let authAndRegForm = wrapper.querySelector(".auth_and_reg_form");
		
		wrapper.style.display = "block";
		authAndRegForm.style.display = "block";
	}})
}

function addEvent() {
	signInBtn.addEventListener("click", openAuthAndRegForm);
}

addEvent();