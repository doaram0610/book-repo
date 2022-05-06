import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css'; //index.js or App.js 에 건다
import 'bootstrap/dist/js/bootstrap.min.js'; //index.js or App.js 에 건다

//store 즉 공통변수 선언을 위해 redux 사용하기
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import reducer from './js/ApiService';
const store = createStore(reducer);

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Provider store={store}>
        <App />
      </Provider>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root'),
);
