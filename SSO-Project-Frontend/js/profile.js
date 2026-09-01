fetch("http://localhost:8080/api/auth/profile", {
    credentials: "include"
})
    .then(response => {

        if (!response.ok) {
            throw new Error("Not authenticated");
        }

        return response.json();
    })

    .then(user => {

        document.getElementById("profilePicture").src =
            user.picture;

        document.getElementById("userName").textContent =
            user.name;

        document.getElementById("userEmail").textContent =
            user.email;

        document.getElementById("phoneNumber").textContent =
            user.phoneNumber || "";

        document.getElementById("department").textContent =
            user.department || "";

        document.getElementById("designation").textContent =
            user.designation || "";
    })

    .catch(error => {

        console.error(error);

        window.location.href = "/index.html";

    });
	
document
    .getElementById("logoutButton")
    .addEventListener("click", async function () {

        try {

            const response = await fetch(
                "http://localhost:8080/logout",
                {
                    method: "POST",
                    credentials: "include"
                }
            );

            console.log("Logout status:", response.status);

            if (response.ok || response.redirected) {
                window.location.href = "http://localhost:5501/index.html";
            }

        } catch (error) {

            console.error("Logout error:", error);

        }

    });