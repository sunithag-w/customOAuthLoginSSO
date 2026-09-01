const BACKEND_URL = "http://localhost:8080";

async function loadOAuthProviders() {

    const container =
        document.getElementById("oauth-buttons");

    try {

        const response = await fetch(
            `${BACKEND_URL}/api/auth/providers`
        );

        if (!response.ok) {

            throw new Error(
                "Failed to load login providers"
            );
        }

        const providers =
            await response.json();

        container.innerHTML = "";


        if (providers.length === 0) {

            container.innerHTML = `
                <p class="text-center text-secondary">
                    No login options currently enabled.
                    Contact administrator.
                </p>
            `;

            return;
        }


        providers.forEach((provider, index) => {

            const button =
                document.createElement("a");


           

            button.href =
                `${BACKEND_URL}/api/auth/login/${provider.id}`;


            button.className =
                "btn btn-light border rounded-3 py-3 " +
                "d-flex align-items-center justify-content-center " +
                "shadow-sm";


            // Provider icon

            const image =
                document.createElement("img");

            image.className = "me-3";

            image.width = 22;
            image.height = 22;


            if (provider.id === "google") {

                image.src =
                    "https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg";

            }
            else if (provider.id === "azure") {

                image.src =
                    "https://cdn.jsdelivr.net/gh/glincker/thesvg@main/public/icons/microsoft/default.svg";

            }
            else if (provider.id === "cognito") {

                image.src =
                    "https://upload.wikimedia.org/wikipedia/commons/9/93/Amazon_Web_Services_Logo.svg";

                image.width = 29;
                image.height = 29;

            }
            else {

                image.src =
                    "https://cdn-icons-png.flaticon.com/512/1828/1828464.png";
            }


            // Provider name

            const text =
                document.createElement("span");

            text.className =
                "fw-semibold";

            text.textContent =
                provider.name;


            button.appendChild(image);

            button.appendChild(text);

            container.appendChild(button);


            // Separator

            if (index < providers.length - 1) {

                const hr =
                    document.createElement("hr");

                hr.className = "my-3";

                container.appendChild(hr);
            }

        });

    }
    catch (error) {

        console.error(
            "Error loading OAuth providers:",
            error
        );

        container.innerHTML = `
            <p class="text-center text-danger">
                Unable to load login providers.
            </p>
        `;
    }
}


loadOAuthProviders();