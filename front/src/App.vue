<template>

  <div id="app">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <nav class="navbar navbar-dark navbar-expand-md" id="app-navbar">
      <div class="container-fluid"><a class="navbar-brand" href="#"><i class="fa fa-life-ring" id="brand-logo"></i></a>
        <button data-toggle="collapse" class="navbar-toggler" data-target="#navcol-1"><span class="sr-only">Toggle navigation</span><span
            class="navbar-toggler-icon"></span></button>
        <div
            class="collapse navbar-collapse" id="navcol-1">
          <ul class="nav navbar-nav ml-auto">
            <li v-if="isLogged" @click="toProfilePage" class="nav-item" role="presentation"><a class="nav-link active">Profile</a>
            </li>
            <li v-if="isLogged" class="nav-item" @click="toExchangePage" role="presentation"><a class="nav-link">Calculator</a>
            </li>
            <li v-if="showAdminBoard" @click="toUsersPage" class="nav-item" role="presentation"><a class="nav-link">Users</a>
            </li>
            <li v-if="isLogged" @click="logOut" class="nav-item" role="presentation"><a class="nav-link">LogOut</a></li>
            <li v-if="!isLogged" @click="toRegistrationPage" class="nav-item" role="presentation"><a
                class="nav-link active">SignUp</a></li>
            <li v-if="!isLogged" @click="toLoginPage" class="nav-item" role="presentation"><a class="nav-link active">LogIn</a>
            </li>

          </ul>
        </div>
      </div>
    </nav>

    <div class="container">
      <router-view/>
    </div>
  </div>
</template>

<script>

import {handleLogOut, isAdmin, isAuthenticated} from "@/utils/utils";

export default {
  name: 'App',
  components: {},
  computed: {
    showAdminBoard() {
      return isAdmin();
    },

    isLogged() {
      return isAuthenticated();
    }

  },
  methods: {
    logOut() {
      handleLogOut()
      this.$router.go("/login");
    },
    toUsersPage() {
      this.$router.push("/admin/users");
    },
    toExchangePage() {
      this.$router.push("/exchange");
    },
    toRegistrationPage() {
      this.$router.push("/register");
    },
    toLoginPage() {
      this.$router.push("/login");
    },
    toProfilePage() {
      this.$router.push("/info");
    }
  }
};
</script>

<style>#brand-logo {
  font-size: 34px;
}

#app-navbar {
  background: linear-gradient(120deg, #0cf9e5, #5a84eb);
}

.nav-link {
  cursor: pointer
}


body {
  background: #eee;
}</style>


