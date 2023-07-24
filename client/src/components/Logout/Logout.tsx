import { styled } from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { removeCookie, getCookie } from '../../util/cookie/index.ts';
import { useDispatch } from 'react-redux';
import { logout } from '../../store/userSlice.ts';
import { locationLogout } from '../../store/locationSlice.ts';
import axios from 'axios';

interface ISHowToggle {
  setShowToggleMenu: React.Dispatch<React.SetStateAction<boolean>>;
}

const Logout = ({ setShowToggleMenu }: ISHowToggle) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const doLogout = async () => {
    await axios.delete(`${import.meta.env.VITE_APP_API_URL}/auth/logout`, {
      headers: { Refresh: getCookie('refreshToken') },
    });
    removeCookie('accessToken');
    removeCookie('refreshToken');
    localStorage.clear();
    dispatch(
      logout({
        memberId: null,
        isLogin: false,
        email: null,
      })
    );

    dispatch(locationLogout({ locationId: null }));

    navigate('/'); //로그인 유저가 바뀔 때 발생하는 버그를 막기위해 reload설정
  };
  return (
    <LogoutButton
      onClick={() => {
        doLogout();
        setShowToggleMenu(false);
      }}
    >
      로그아웃
    </LogoutButton>
  );
};

export default Logout;

const LogoutButton = styled.button`
  cursor: pointer;
`;
