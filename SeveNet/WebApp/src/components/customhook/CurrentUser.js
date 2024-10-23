import {useAuth} from "../../services/AuthContext";

const useCurrentUser = () => {
    const {currentUser, setCurrentUse} = useAuth();
    return currentUser;
};

export default useCurrentUser;
