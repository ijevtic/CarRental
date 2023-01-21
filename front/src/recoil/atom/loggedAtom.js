import { atom } from "recoil";

export const profileInfo = atom({
  key: "profileState",
  default: {
    'loggedIn':'false',
    'jwt': null,
    'role': null,
    'data': null,
  }
});