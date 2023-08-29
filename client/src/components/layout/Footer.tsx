import { Link } from 'react-router-dom';
import { styled } from 'styled-components';

const Footer = () => {
  return (
    <FooterContainer>
      <FooterPositioner>
        <Link to="/introduction">
          <IntroLink>About 밥친구</IntroLink>
        </Link>
        <FooterContents>
          <svg height="15" width="20" color="#555555">
            <path d="M8 0c4.42 0 8 3.58 8 8a8.013 8.013 0 0 1-5.45 7.59c-.4.08-.55-.17-.55-.38 0-.27.01-1.13.01-2.2 0-.75-.25-1.23-.54-1.48 1.78-.2 3.65-.88 3.65-3.95 0-.88-.31-1.59-.82-2.15.08-.2.36-1.02-.08-2.12 0 0-.67-.22-2.2.82-.64-.18-1.32-.27-2-.27-.68 0-1.36.09-2 .27-1.53-1.03-2.2-.82-2.2-.82-.44 1.1-.16 1.92-.08 2.12-.51.56-.82 1.28-.82 2.15 0 3.06 1.86 3.75 3.64 3.95-.23.2-.44.55-.51 1.07-.46.21-1.61.55-2.33-.66-.15-.24-.6-.83-1.23-.82-.67.01-.27.38.01.53.34.19.73.9.82 1.13.16.45.68 1.31 2.69.94 0 .67.01 1.3.01 1.49 0 .21-.15.45-.55.38A7.995 7.995 0 0 1 0 8c0-4.42 3.58-8 8-8Z" />
          </svg>
          <GithubLink href="https://github.com/LeeJSYS" target="_blank">
            이준석(@LeeJSYS)
          </GithubLink>{' '}
          |{' '}
          <GithubLink href="https://github.com/yyjjvv" target="_blank">
            유지원(@yyjjvv)
          </GithubLink>{' '}
          |{' '}
          <GithubLink href="https://github.com/thejsw" target="_blank">
            조성원(@thejsw)
          </GithubLink>{' '}
          |{' '}
          <GithubLink href="https://github.com/jmjgirl" target="_blank">
            정민지(@jmjgirl)
          </GithubLink>{' '}
          |{' '}
          <GithubLink href="https://github.com/tjtmddk720" target="_blank">
            서승아(@tjtmddk720)
          </GithubLink>{' '}
          |{' '}
          <GithubLink href="https://github.com/spring333" target="_blank">
            이보미(@spring333)
          </GithubLink>
        </FooterContents>
        <FooterContents>Copyright©2023 BabChingu.All rights reserved.</FooterContents>
      </FooterPositioner>
    </FooterContainer>
  );
};

const FooterContainer = styled.footer`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 9.375rem;
  background-color: var(--color-beige);
`;

const FooterPositioner = styled.div`
  height: 5rem;
`;

const IntroLink = styled.span`
  display: block;
  width: 100px;
  margin: 0 auto 20px;
  color: var(--color-black);
  text-align: center;
  font-size: 12px;
`;

const FooterContents = styled.div`
  margin-bottom: 20px;
  color: var(--color-black);
  text-align: center;
  font-size: 12px;
`;

const GithubLink = styled.a`
  color: var(--color-black);
`;

export default Footer;
