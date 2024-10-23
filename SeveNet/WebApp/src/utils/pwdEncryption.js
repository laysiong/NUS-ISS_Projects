import CryptoJS from 'crypto-js';

// encrypt pwd at front end
// employee's user_name should be unique, so we take their uname as key
// why not use id? because when we create user they don't have their id yet
// So we use the same key in back end
const key = CryptoJS.enc.Utf8.parse("20240202team7666"); // 16 byte length key

const encryptUserPassword = (password) => {
    const encryptedPassword = CryptoJS.AES.encrypt(password, key, {
        mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
    }).toString();
    return encryptedPassword;
};

const decryptUserPassword = (password) => {
    const decryptedPassword = CryptoJS.AES.decrypt(password, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString(CryptoJS.enc.Utf8);
    return decryptedPassword;
};


// eslint-disable-next-line import/no-anonymous-default-export
export default { encryptUserPassword,decryptUserPassword };