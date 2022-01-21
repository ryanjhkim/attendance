import logo from './logo.svg';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import { Button, Form, Card } from 'react-bootstrap'

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Card className="mb-3" style={{ color: "#000", width: '25rem', height: '20rem'}}>
          <Card.Title>CPSC 213 Attendance</Card.Title>
        <Form>
          <Form.Group>
            <Form.Label>Student Number</Form.Label>
            <Form.Control type="email" placeholder="Enter the 8 digits of your Student Number" />
          </Form.Group>
          <Form.Group controlID="formPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password" />
          </Form.Group>
          
          <Button variant="primary" type="submit">Sumbit</Button>
        </Form>
        </Card>
      </header>
    </div>
  );
}

export default App;
