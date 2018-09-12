import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import classNames from "classnames";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import axios from "axios";
import Avatar from "@material-ui/core/Avatar";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";

const styles = {
  root: {
    width: "100%",
    overflowX: "auto"
  },
  card: {
    maxWidth: 345,
    textAlign: "center"
  },
  media: {
    height: 140
  },
  cardActionArea: {
    textAlign: "center"
  },
  avatar: {
    margin: 10
  },
  bigAvatar: {
    width: 70,
    height: 70
  }
};

const client = axios.create({
  baseURL: "http://localhost:8080/api/",
  timeout: 2000,
  headers: { "Content-Type": "application/json" }
});

class SimpleTable extends React.Component {
  state = {
    page: 0,
    size: 20,
    data: []
  };

  async componentDidMount() {
    const response = await client.get(
      `/sitters?size=${this.state.size}&page=${this.state.page}&sort=rank,desc`
    );
    console.log(response.data._embedded.sitters);
    this.setState({ data: response.data._embedded.sitters });
    // data = response.data._embedded.sitters;
  }

  round(val) {
    return Number(val).toFixed(1);
  }

  render() {
    const { classes } = this.props;

    return (
      <Grid container className={classes.root} spacing={24}>
        {this.state.data.map(n => {
          return (
            <Grid item key={n.email} xs={12} sm={6} md={3}>
              <Card className={classes.card}>
                <CardActionArea>
                  <CardContent>
                    <Grid container>
                      <Grid item>
                        <Avatar
                          alt={n.name}
                          src={n.image}
                          className={classNames(
                            classes.avatar,
                            classes.bigAvatar
                          )}
                        />
                      </Grid>
                      <Grid item>
                        <Typography
                          gutterBottom
                          variant="headline"
                          component="h2" >
                          {n.name}
                        </Typography>
                        <Typography component="em">
                          Rating: {this.round(n.rating)}
                        </Typography>
                      </Grid>
                    </Grid>
                  </CardContent>
                </CardActionArea>
                <CardActions>
                  <Button size="small" color="primary">
                    Share
                  </Button>
                  <Button size="small" color="primary">
                    Contact Sitter
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          );
        })}
      </Grid>
    );
    // <Paper className={classes.root}>
    //   <Table className={classes.table}>
    //     <TableHead>
    //       <TableRow>
    //         <TableCell>Name</TableCell>
    //         <TableCell>Photo</TableCell>
    //         <TableCell numeric>Rating</TableCell>
    //       </TableRow>
    //     </TableHead>
    //     <TableBody>
    //       {this.state.data.map(n => {
    //         return (
    //           <TableRow key={n.email}>
    //             <TableCell component="th"
    //                        scope="row">
    //               {n.name}
    //             </TableCell>
    //             <TableCell>{n.image}</TableCell>
    //             <TableCell numeric>{this.round(n.rating)}</TableCell>
    //           </TableRow>
    //         );
    //       })}
    //     </TableBody>
    //   </Table>
    // </Paper>
  }
}

SimpleTable.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(SimpleTable);
