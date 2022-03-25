import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {BrowserRouter} from 'react-router-dom';
import  'bootstrap/dist/css/bootstrap.min.css' ;  //index.js or App.js 에 건다
import  'bootstrap/dist/js/bootstrap.min.js' ;  //index.js or App.js 에 건다

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root')
);