<template>
    <v-container class="login-container">
        <v-img class="mx-auto my-6" :height="100" :src="image" alt="Tinkerstal logo"></v-img>
        <v-card style="background-color: transparent;" class="mx-auto pb-8 form-container" elevation="0" max-width="448"
            rounded="lg">

            <v-text-field v-model="newPassword" :append-inner-icon="visible ? 'mdi:mdi-eye-off' : 'mdi:mdi-eye'"
                :type="visible ? 'text' : 'password'" density="compact" placeholder="Geef een nieuw wachtwoord"
                prepend-inner-icon="mdi:mdi-lock-outline" variant="outlined" required
                @click:append-inner="visible = !visible"></v-text-field>

            <v-text-field v-model="confirmPassword" :append-inner-icon="visible ? 'mdi:mdi-eye-off' : 'mdi:mdi-eye'"
                :type="visible ? 'text' : 'password'" density="compact" placeholder="Bevestig nieuw wachtwoord"
                prepend-inner-icon="mdi:mdi-lock-outline" variant="outlined" required
                @click:append-inner="visible = !visible"></v-text-field>


            <v-btn @click="submitForm" class="mb-8" color="white" size="large" variant="tonal" block>
                Wachtwoord wijzigen
            </v-btn>
        </v-card>
    </v-container>
</template>

<script>

import image from '@/assets/LogoTinkerstal2.png'

export default {
    name: 'ResetPasswordPage',
    data() {
        return {
            image: image,
            newPassword: '',
            confirmPassword: '',
            visible: false,
            token: '',
        };
    },
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        this.token = urlParams.get('token');
        console.error(this.token);

    },
    methods: {
        async submitForm() {
            if (this.newPassword === "" || this.confirmPassword === "") {
                //this.$toast.error("Please fill in both fields.");
                alert("Please fill in both fields.");
                return;
            }
            if (this.newPassword !== this.confirmPassword) {
                //this.$toast.error("Passwords do not match.");
                alert("Passwords do not match.");
                return;
            }

            try {
                const response = await fetch("http://localhost:8082/api/password/update-password", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        token: this.token,
                        newPassword: this.newPassword,
                    }),
                });

                if (!response.ok) {
                    throw new Error("Failed to reset password.");
                }
                // this.$toast.success("Password successfully reset. You can now log in with your new password.");
                alert("Password successfully reset. You can now log in with your new password.");
                this.$router.push({ name: 'login' });
            } catch (error) {
                // this.$toast.error("There was an error resetting your password. Please try again.");
                alert("There was an error resetting your password. Please try again.");
            }
        }
    }
};

</script>