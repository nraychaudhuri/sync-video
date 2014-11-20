iAmControlling = ->
  $_("#controller").innerHTML is $_("#name").value
$_ = (sel) ->
  document.querySelector sel
controller = null


window.onload = ->
  $_("#name").value = "user" + parseInt(99999 * Math.random())  if $_("#name").value is ""
  $_("#join").onclick = ->
    conn = new WebSocket("ws://localhost:9000/ws/" + $_("#name").value)
    $_("#username").innerHTML = $_("#name").value
    $_("#room").className = "active"
    $_("#registration").className = "inactive"
    video = $_("video")
    video.addEventListener "timeupdate", (->
      conn.send JSON.stringify({action: "currentTime", value: "" + video.currentTime})  if iAmControlling()
      return
    ), true
    video.addEventListener "pause", (->
      conn.send JSON.stringify({action: "pause", value:"pause " + video.currentTime})  if iAmControlling()
      return
    ), true
    video.addEventListener "play", (->
      conn.send JSON.stringify({action: "play", value: "play " + video.currentTime})  if iAmControlling()
      return
    ), true
    conn.onmessage = (ev) ->
      data = JSON.parse(ev.data).value
      matches = undefined
      if matches = data.match(/^control (.+)$/)
        $_("#controller").innerHTML = matches[1]
      else if matches = data.match(/^userCount (.+)$/)
        document.getElementById("userCount").innerHTML = matches[1]
      else if matches = data.match(/^pause (.+)$/)
        console.log data
        video.currentTime = matches[1]
        video.pause()
      else if matches = data.match(/^play (.+)$/)
        console.log data
        video.currentTime = matches[1]
        video.play()
      else
        return  if iAmControlling()
        estimatedTimeOnMaster = parseInt(data) + 1
        video.currentTime = estimatedTimeOnMaster  if Math.abs(estimatedTimeOnMaster - video.currentTime) > 5
      return

    $_("#takeControl").onclick = (ev) ->
      conn.send JSON.stringify({action: "control", value: "control " + $_("#name").value})
      return

    $_("#leave").onclick = ->
      conn.close()
      $_("#room").className = "inactive"
      $_("#registration").className = "active"
      return

    $_("#like").onclick = ->
      conn.send JSON.stringify({action: "like", value: video.currentTime})
      return

    return

  return