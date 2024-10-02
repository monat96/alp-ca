<script setup lang="ts">
import { Input } from "@/shared/ui/input";
import { Button } from "@/shared/ui/button";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from "@/shared/ui/form";
import { useForm } from "vee-validate";
import { toTypedSchema } from "@vee-validate/zod";
import { login, LoginFormSchema, useAuthStore } from "@/entities/user";
import { useRouter } from "vue-router";

const router = useRouter();
const { setTokens } = useAuthStore();
const loginFormSchema = toTypedSchema(LoginFormSchema);

const { isFieldDirty, handleSubmit } = useForm({
  initialValues: {
    username: "",
    password: "",
  },
  validationSchema: loginFormSchema,
});

const onSubmit = handleSubmit(
  values => login(values)
    .then(response => setTokens(response!.data!))
    .then(() => router.replace("/"))
    .catch(console.error)
);

</script>
<template>
  <form @submit="onSubmit" class="flex flex-col gap-4 w-1/2">
    <h1 class="text-3xl font-bold mb-4">Login</h1>
    <FormField name="username" v-slot="{ componentField }">
      <FormItem>
        <FormLabel>Username</FormLabel>
        <FormControl>
          <Input type="text" placeholder="Enter your username" autocomplete="username" v-bind="componentField"
            :validate-on-blur="!isFieldDirty" />
        </FormControl>
      </FormItem>
    </FormField>
    <FormField name="password" v-slot="{ componentField }">
      <FormItem>
        <FormLabel>Password</FormLabel>
        <FormControl>
          <Input type="password" placeholder="Enter your password" autocomplete="current-password"
            v-bind="componentField" :validate-on-blur="!isFieldDirty" />
        </FormControl>
      </FormItem>
    </FormField>
    <Button class="w-full" type="submit">
      Login
    </Button>
  </form>
</template>