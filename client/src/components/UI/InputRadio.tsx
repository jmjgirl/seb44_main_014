import { styled } from 'styled-components';

type ChildrenProps = {
  children: string;
  type: string;
  value: string;
  handleGetValue: (e: React.MouseEvent<HTMLInputElement>) => void;
};

const InputRadio = ({ children, type, value, handleGetValue }: ChildrenProps) => {
  return (
    <InputLabel htmlFor={children}>
      {children}
      <RadioInput
        type="radio"
        name={type}
        id={children}
        value={value}
        onClick={(e: React.MouseEvent<HTMLInputElement>) => handleGetValue(e)}
      />
      <Checkmark />
    </InputLabel>
  );
};

const InputLabel = styled.label`
  position: relative;
  display: block;
  padding-left: 1.875rem;
  font-size: 0.875rem;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;

  input[type='radio']:checked ~ span {
    background-image: url('/img/radio_selected.png');
  }
  input[type='radio']:disabled ~ span {
    background-image: url('/img/radio_disabled.png');
  }
`;

const RadioInput = styled.input`
  position: absolute;
  height: 0;
  width: 0;
  opacity: 0;
  cursor: pointer;
`;

const Checkmark = styled.span`
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  height: 20px;
  width: 20px;
  background-image: url('/img/radio_unselected.png');
  background-size: cover;
  border-radius: 50%;
`;

export default InputRadio;
