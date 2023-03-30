# frozen_string_literal: true
require './dom/base/jenkins_job.rb'
require './dom/build_brunch_history.rb'

def main(rootDir, jobName)
  job = JenkinsJob.new(rootDir, jobName)

  if !job.exist?
    puts "指定したジョブは存在しません:#{job.jobName}"
    return
  end

  build_history = BuildBrunchHistory.load(job)
  build_history.build_brunches.each do |brunch|
    last_build = build_history.last_build(brunch)

    if(last_build.success?)
      puts(brunch)
    end
  end
end

main(ARGV[0], ARGV[1])
