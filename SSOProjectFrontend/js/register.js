

fetch("http://localhost:8080/api/auth/registration-data", {
    credentials: "include"
})
    .then(response => {

        if (!response.ok) {
            throw new Error("Unable to load user data");
        }

        return response.json();
    })
    .then(user => {

        document.getElementById("name").value = user.name || "";
        document.getElementById("email").value = user.email || "";

        if (user.picture) {
            document.getElementById("profilePicture").src =
                user.picture;
        }
    })
    .catch(error => {

        console.error("Error loading user data:", error);

        const errorMessage =
            document.getElementById("errorMessage");

        errorMessage.textContent =
            "Unable to load user data.";

        errorMessage.classList.remove("d-none");
    });


// ===============================
// Registration
// ===============================

document
    .getElementById("registerForm")
    .addEventListener("submit", function (event) {

        event.preventDefault();

        const data = {

            phoneNumber:
                document.getElementById("phoneNumber").value,

            department:
                document.getElementById("department").value,

            designation:
                document.getElementById("designation").value
        };


        // Send registration request
        fetch("http://localhost:8080/api/auth/register", {

            method: "POST",

            credentials: "include",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(data)
        })

        .then(response => {

            if (!response.ok) {
                throw new Error("Registration failed");
            }

            return response.json();
        })

        .then(user => {

            console.log("Registration successful:", user);

            // Go to profile page
            window.location.href =
                "http://localhost:5501/profile.html";
        })

        .catch(error => {

            console.error("Registration error:", error);

            const errorMessage =
                document.getElementById("errorMessage");

            errorMessage.textContent =
                error.message;

            errorMessage.classList.remove("d-none");
        });

    });