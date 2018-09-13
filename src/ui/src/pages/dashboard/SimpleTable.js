import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";

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

class SimpleTable extends React.Component {
  state = {};

  round(val) {
    return Number(val).toFixed(1);
  }

  render() {
    const { classes } = this.props;

    return (
      <React.Fragment>
        <Grid container className={classes.root} spacing={16}>
          {this.props.data.map(n => {
            return (
              <Grid item key={n.email} xs={12} sm={6} md={4} lg={2}>
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
                            component="h2"
                          >
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
      </React.Fragment>
    );
  }
}

SimpleTable.propTypes = {
  classes: PropTypes.object.isRequired,
  data: PropTypes.array.isRequired
};

export default withStyles(styles)(SimpleTable);
