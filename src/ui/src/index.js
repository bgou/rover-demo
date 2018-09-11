import React from 'react';
import ReactDOM from 'react-dom';
import Dashboard from './pages/dashboard/Dashboard';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<Dashboard />, document.querySelector('#root'));
registerServiceWorker();
