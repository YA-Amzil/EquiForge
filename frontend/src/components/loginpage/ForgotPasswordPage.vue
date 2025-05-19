<template>
    <v-container class="login-container">
        <v-img class="mx-auto my-6" :height="100" :src="image" alt="Tinkerstal logo"></v-img>
        <v-card style="background-color: transparent;" class="mx-auto pb-8 form-container" elevation="0" max-width="448"
            rounded="lg">

            <v-text-field v-model="email" density="compact" placeholder="E-mailadres" prepend-inner-icon="mdi:mdi-at"
                variant="outlined" required>
            </v-text-field>

            <v-card class="mb-12" color="surface-variant" variant="tonal">
            </v-card>

            <v-btn @click="submitForm" class="mb-8" color="white" size="large" variant="tonal" block>
                stuur verzoek
            </v-btn>
            <v-btn class="mb-8" color="white" variant="tonal" size="large" block to="/login">
                Anuleren
            </v-btn>
        </v-card>
    </v-container>
</template>

<script>

import image from '@/assets/LogoTinkerstal2.png'

export default {
    name: "ForgotPasswordPage",
    data() {
        return {
            image: image,
            email: "",
        };
    },
    methods: {
        async submitForm() {
            if (this.email === "") {
                // this.$toast.error("Please enter your email address.");
                // return;
                alert("Please enter your email address.");
            }
            try {
                const response = await fetch("http://localhost:8082/api/password/reset-request", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ email: this.email }),
                });

                if (!response.ok) {
                    throw new Error("Failed to send password reset request.");
                }

                //this.$toast.success("Password reset link has been sent to your email.");
                alert("Password reset link has been sent to your email.");
            } catch (error) {
                //this.$toast.error("There was an error sending the reset request. Please try again.");
                alert("There was an error sending the reset request. Please try again.");
            }
        },
    },
};
</script>