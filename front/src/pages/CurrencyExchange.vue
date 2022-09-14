<template>

  <div>
    <div class="card card-container">
      <div class="currency-row-outer">
        <div class="currency-converter">
          <h2>Currency Converter</h2>


          <div class="field grid-30-30-30">
            <label>Amount</label>
            <div class="colmun col-center">
              <label> From</label>
            </div>
            <div class="colmun col-right">
              <label> To</label>
            </div>
          </div>

          <div class="field grid-30-30-30">
            <div class="colmun col-left">
              <input v-model="exchangeData.baseAmount" type="number" @change="validate" class="form-input" id="number"
                     placeholder="0,00">
            </div>
            <div class="colmun col-right">
              <div class="select">
                <select v-model="exchangeData.baseCurrency" @change="changeBaseCurrency" class="currency"
                        name="currency">
                  <option disabled value="">Select</option>
                  <option
                      v-for="option in options"
                      :key="option.id"
                      :value="option.value">
                    {{ option.value }}
                  </option>
                </select>

              </div>
            </div>
            <div class="colmun col-right">
              <div class="select">
                <select v-model="exchangeData.newCurrency" @change="changeNewCurrency" class="currency" name="currency">
                  <option disabled value="">Select</option>
                  <option
                      v-for="option in options"
                      :key="option.id"
                      :value="option.value">
                    {{ option.value }}
                  </option>
                </select>

              </div>
            </div>
          </div>
          <label id="hidden" v-if="newAmount">{{ newAmount }}</label>
          <label id="error" v-if="error">{{ error }}</label>

          <button @click="convert" class="btn btn-primary btn-block">
            <span>Convert</span>
          </button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import {currencyOptions, getToken} from "@/utils/utils";
import axios from "axios";

export default {
  name: "CurrencyExchange",
  data() {
    return {
      options: currencyOptions(),
      exchangeData: {
        baseCurrency: "",
        newCurrency: "",
        baseAmount: 0
      },
      newAmount: "",
      error : ""
    }

  },
  methods: {
    changeBaseCurrency(event) {
      if (event.target.value !== this.exchangeData.newCurrency) {
        this.exchangeData.baseCurrency = event.target.value;
        this.error = ""
      } else {
        this.exchangeData.baseCurrency = ""
      }

    },

    changeNewCurrency(event) {
      console.log(event.target.value)
      console.log(this.exchangeData.baseCurrency)
      if (event.target.value !== this.exchangeData.baseCurrency) {
        this.error = ""
        this.exchangeData.newCurrency = event.target.value;
      } else {
        this.exchangeData.newCurrency = ""
      }
    },

    validate() {
      this.exchangeData.baseAmount = parseFloat(this.exchangeData.baseAmount).toFixed(2);
      this.error = ""
    },

    convert() {
      if (this.exchangeData.baseAmount != 0 && this.exchangeData.baseAmount && this.exchangeData.newCurrency) {
        console.log(this.exchangeData)

        axios.post("http://localhost:8080/currency",
            this.exchangeData, {headers: {'Authorization': 'Bearer ' + getToken()}}).then((resp) => {
          this.newAmount = resp.data.newAmount.toFixed(2)
          console.log(resp);
        }).catch(error => {
          this.error = error.response.data.message;
        })
      }
      else {
        this.error = "Please fill all the fields"
      }
    }
  }
}
</script>

<style scoped>

* {
  padding: 0;
  margin: 0;
  font-family: 'IBM Plex Sans', sans-serif;
}

body {
  height: 100vh;
  width: 100vw;
  overflow-x: hidden;
}

.currency-row-outer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.currency-converter {
  width: 100%;
  max-width: 580px;
  text-align: center;
}

#hidden {
  padding-top: 10px;
  padding-bottom: 10px;
  font-size: 80px;
}

input,
select {
  color: #363636;
  font-size: 1rem;
  height: 2.3em;
  border-radius: 4px;
  max-width: 100%;
  width: calc(100% - 15px);
  margin: auto;
  outline: 0;
  background: #fff;
  border-color: #dbdbdb;
  padding-left: 15px;
  border: 1px solid #00000057;
  box-shadow: inset 0 0.0625em 0.125em rgb(10 10 10 / 5%);
  -webkit-appearance: none;
}

.field.grid-30-30-30 {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 15px;
}

.currency-converter .colmun {
  margin-bottom: 15px;
}

select.currency {
  border-color: #3273dc;
  width: 100%;
  height: 100%;
  cursor: pointer;
  font-size: 1em;
  max-width: 100%;
  outline: 0;
  display: block;
}

.currency-converter .select {
  position: relative;
  height: 100%;
}

h2 {
  padding-bottom: 30px;
}

.currency-converter .select:after {
  transform: rotate(-45deg);
  transform-origin: center;
  content: "";
  border: 3px solid rgba(0, 0, 0, 0);
  border-radius: 2px;
  border-top: 0;
  border-right: 0;
  display: block;
  height: 0.525em;
  width: 0.525em;
  z-index: 4;
  position: absolute;
  top: 50%;
  right: 1.125em;
  margin-top: -0.4375em;
}

.select:not(:hover)::after {
  border-color: #3273dc;
}

.select:hover::after {
  border-color: #363636;
}

.card-container.card {
  max-width: 50%;
  padding: 40px 40px;
}

.card {
  /*background-color: #f7f7f7;*/
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

#error {
  color: red
}

</style>