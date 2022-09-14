<template>
  <div>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <div class="container">
      <div class="table-wrapper">
        <div class="table-title">
          <div class="row">
            <div class="col-sm-6">
              <h2>Users</h2>
            </div>

          </div>
        </div>
        <table class="table table-striped table-hover">
          <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Address</th>
            <th>IsActive</th>
            <th>Birthdate</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ fullName(user) }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.address }}</td>
            <td>{{ user.isActive }}</td>
            <td>{{ user.birthdate }}</td>
            <td>
              <button v-if="user.isActive" @click="openUserPage(user)"
                      class="edit" data-toggle="modal"><i class="material-icons"
                                                          data-toggle="tooltip" title="Edit">&#xE254;</i></button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>



</template>

<script>
import axios from "axios";
import {getToken} from "@/utils/utils";

export default {
  name: "AdminPage",
  data() {
    return {
      users: [],
      options: [{value: "USD", id: 1}, {value: "EUR", id: 2}],
      selectedOption: ""
    }

  },
  mounted() {
    this.fetchUsers()
  },
  methods: {
    fetchUsers() {
      axios.get("http://localhost:8080/admin/users",
          {headers: {'Authorization': 'Bearer ' + getToken()}}).then(resp => {
        this.users = resp.data.users
      })
    },

    fullName(user) {
      console.log(user)
      let fname = user.firstName ?? '';
      let lname = user.lastName ?? '';
      let name = fname + " " + lname
      return name.trim()
    },

    openUserPage(user) {
      this.$router.push("/admin/users/edit/" + user.id);
    },

    changeOption(event) {
      this.selectedOption = event.target.value;
      console.log(this.selectedOption)
    }
  }
}
</script>

<style scoped>
body {
  color: #566787;
  background: #f5f5f5;
  font-family: 'Varela Round', sans-serif;
  font-size: 13px;
}

.table-wrapper {
  background: #fff;
  padding: 20px 25px;
  margin: 30px 0;
  border-radius: 3px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
}

.table-title {
  padding-bottom: 15px;
  background: #41c1d2;
  /*background: linear-gradient(120deg, #0cf9e5, #5a84eb);*/
  color: #fff;
  padding: 16px 30px;
  margin: -20px -25px 10px;
  border-radius: 3px 3px 0 0;
}

.table-title h2 {
  margin: 5px 0 0;
  font-size: 24px;
}

.table-title .btn-group {
  float: right;
}

.table-title .btn {
  color: #fff;
  float: right;
  font-size: 13px;
  border: none;
  min-width: 50px;
  border-radius: 2px;
  border: none;
  outline: none !important;
  margin-left: 10px;
}

.table-title .btn i {
  float: left;
  font-size: 21px;
  margin-right: 5px;
}

.table-title .btn span {
  float: left;
  margin-top: 2px;
}

table.table tr th, table.table tr td {
  border-color: #e9e9e9;
  padding: 12px 15px;
  vertical-align: middle;
}

table.table tr th:first-child {
  width: 60px;
}

table.table tr th:last-child {
  width: 100px;
}

table.table-striped tbody tr:nth-of-type(odd) {
  background-color: #fcfcfc;
}

table.table-striped.table-hover tbody tr:hover {
  background: #f5f5f5;
}

table.table th i {
  font-size: 13px;
  margin: 0 5px;
  cursor: pointer;
}

table.table td:last-child i {
  opacity: 0.9;
  font-size: 22px;
  margin: 0 5px;
}

table.table td button {
  font-weight: bold;
  color: #566787;
  display: inline-block;
  text-decoration: none;
  outline: none !important;
  background: transparent;
  border: transparent;
}

table.table td button:hover {
  color: #2196F3;
}

table.table td button.edit {
  color: #FFC107;
}

table.table td button.delete {
  color: #F44336;
}

table.table td i {
  font-size: 19px;
}

table.table {
  border-radius: 50%;
  vertical-align: middle;
  margin-right: 10px;
}

.modal .modal-dialog {
  max-width: 400px;
}

.modal .modal-header, .modal .modal-body, .modal .modal-footer {
  padding: 20px 30px;
}

.modal .modal-content {
  border-radius: 3px;
}

.modal .modal-footer {
  background: #ecf0f1;
  border-radius: 0 0 3px 3px;
}

.modal .modal-title {
  display: inline-block;
}

.modal .form-control {
  border-radius: 2px;
  box-shadow: none;
  border-color: #dddddd;
}

.modal textarea.form-control {
  resize: vertical;
}

.modal .btn {
  border-radius: 2px;
  min-width: 100px;
}

.modal form label {
  font-weight: normal;
}

</style>