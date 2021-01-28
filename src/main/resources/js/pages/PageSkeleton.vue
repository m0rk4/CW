<template>
  <v-app>
    <v-app-bar app>
      <v-app-bar-title>Reviewer</v-app-bar-title>
      <v-spacer></v-spacer>

      <v-btn ref="main" href="/" icon>
        <v-icon>mdi-magnify</v-icon>
      </v-btn>

      <v-divider vertical inset></v-divider>

      <div v-if="!profile">
        <login-dialog></login-dialog>
        <register-dialog></register-dialog>
      </div>
      <div v-else>
        <v-btn text
               rounded
               class="mx-2"
               @click="$router.push('/user')"
               :disabled="$router.currentRoute.path === '/user'"
        >
          <v-avatar size="36" class="mr-1">
            <img :src="profile.userpic">
          </v-avatar>
          {{ profile.name }}
        </v-btn>
        <v-btn depressed rounded v-if="isAdmin" @click="$router.push('/admin')">Admin</v-btn>
        <v-btn href="/logout" icon>
          <v-icon>mdi-export</v-icon>
        </v-btn>
      </div>
    </v-app-bar>


    <v-main class="my-4">
      <v-container>
        <router-view></router-view>
      </v-container>
    </v-main>

    <v-alert class="ml-auto mr-4"
             dismissible
             dense
             :type="typeAlert"
             :value="visibilityAlert"
             transition="scale-transition"
    >
      {{ messageAlert }}
    </v-alert>

    <v-footer padless>
      <v-col
          class="text-center"
          cols="12"
      >
        {{ new Date().getFullYear() }} — <strong>Reviewer</strong>
      </v-col>
    </v-footer>
  </v-app>
</template>

<script>
import LoginDialog from "components/auth/LoginDialog.vue";
import RegisterDialog from "components/auth/RegisterDialog.vue";
import {mapState, mapGetters, mapMutations} from 'vuex'
import {addHandler} from "util/ws";

export default {
  components: {LoginDialog, RegisterDialog},
  computed: {
    ...mapState({
      messageAlert: state => state.alert.message,
      typeAlert: state => state.alert.type,
      visibilityAlert: state => state.alert.isVisible,
      profile: state => state.auth.profile,
    }),
    ...mapGetters('auth', [
      'isAdmin',
    ])
  },
  methods: {
    ...mapMutations('item', ['addCommentMutation', 'updateItemMutation'])
  },
  created() {
    addHandler(data => {
          if (data.objectType === 'COMMENT') {
            switch (data.eventType) {
              case 'CREATE':
                this.addCommentMutation(data.body)
                break
              default:
                console.error(`Incorrect evType: ${data.eventType}`)
            }
          } else if (data.objectType === 'LIKE') {
            switch (data.eventType) {
              case 'CREATE':
                this.updateItemMutation(data.body)
                break
              case 'REMOVE':
                this.updateItemMutation(data.body)
                break
              default:
                console.error(`Incorrect evType: ${data.eventType}`)
            }
          } else {
            console.error(`Incorrect objType: ${data.objectType}`)
          }
        }
    )
  }
}
</script>

<style scoped>

</style>