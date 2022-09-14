<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img
          id="profile-img"
          src="../assets/userIcon.png"
          class="profile-img-card"
      />
      <label id="error" v-if="error">{{ error }}</label>
      <form @submit.prevent>
        <div class="form-group">
          <label for="email">Email</label>
          <input v-model="loginData.email" name="email" type="text" class="form-control"/>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input v-model="loginData.password" name="password" type="password" class="form-control"/>
        </div>

        <div class="form-group">
          <button @click="handleLogin" class="btn btn-primary btn-block">
            <span>Login</span>
          </button>
        </div>

      </form>
    </div>
  </div>
</template>

<script>


import axios from "axios";

export default {
  name: "sign-in",
  components: {},
  data() {
    return {
      loginData: {
        email: '',
        password: ''
      },
      error: ''
    }
  },
  methods: {
    handleLogin() {
      axios.post("http://localhost:8080/auth/login", this.loginData).then((resp) => {
        localStorage.setItem("Bearer", resp.data.token)
        localStorage.setItem("Role", resp.data.roles)
        this.$router.go("exchange");
        console.log(resp)
      }).catch(error=>{
        this.error = error.response.data.message;
        console.log(error.response.data.message)
      })
    }
  }
}
</script>

<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}

#error{color: red}
</style>