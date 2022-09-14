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

        <div class="field grid-50-50">
          <div class="form-group">
            <label for="firstName">First Name</label>
            <input v-model="userData.firstName" name="firstName" type="text" class="form-control"/>
          </div>
          <div class="form-group">
            <label for="lastName">Last Name</label>
            <input v-model="userData.lastName" name="lastName" type="text" class="form-control"/>
          </div>
        </div>


        <div class="field grid-50-50">
          <div class="form-group">
            <label for="address">Address</label>
            <input v-model="userData.address" name="address" type="text" class="form-control"/>
          </div>
          <div class="form-group">
            <label for="birthdate">Birthdate</label>
            <input v-model="userData.birthdate" name="birthdate" type="date" class="form-control"/>
          </div>
        </div>

        <div class="form-group">
          <button @click="sendUserUpdate" class="btn btn-primary btn-block">
            <span>Update data</span>
          </button>
        </div>

      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {getToken} from "@/utils/utils";

export default {
  name: "AdminEdit",
  data() {
    return {
      userData: {
        email: '',
        firstName: '',
        lastName: '',
        birthdate: '',
        address: ''
      },
      error: ''
    }
  },
  mounted() {
    this.loadUser()
  },
  methods: {
    loadUser() {
      const id = this.$route.params.id
      axios.get("http://localhost:8080/admin/users/" + id + "/info",
          {headers: {'Authorization': 'Bearer ' + getToken()}}).then((resp) => {
        this.userData = resp.data
        console.log(resp)
      })
    },
    sendUserUpdate() {
      console.log(this.User)
      axios.post("http://localhost:8080/admin/users/edit", this.User,
          {headers: {'Authorization': 'Bearer ' + getToken()}}).then(() => {
        this.$router.push("/admin/users");
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
  max-width: 550px !important;
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

.field.grid-50-50 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 15px;
}

#error {
  color: red
}

</style>